package com.frontier.job.core.server;

import com.frontier.job.core.connect.ServerClient;
import com.frontier.job.core.connect.server.ExecutorServer;
import com.frontier.job.core.model.IdleBeatParam;
import com.frontier.job.core.model.KillParam;
import com.frontier.job.core.model.ReturnT;
import com.frontier.job.core.model.TriggerParam;
import com.frontier.job.core.util.GsonTool;
import com.frontier.job.core.util.JobRemotingUtil;
import com.frontier.job.core.util.ThrowableUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author 王腾飞
 */
public class EmbedServer {
    private static final Logger logger = LoggerFactory.getLogger(EmbedServer.class);

    private Thread thread;

    public void start(final String address, final int port, final String appName, final String accessToken, Scheduler scheduler) {
        ServerClient serverClient = new ExecutorServer(scheduler);
        thread = new Thread(() -> {

            // param
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            ThreadPoolExecutor bizThreadPool = new ThreadPoolExecutor(
                    0,
                    200,
                    60L,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(2000),
                    r -> new Thread(r, "job, EmbedServer bizThreadPool-" + r.hashCode()),
                    (r, executor) -> {
                        throw new RuntimeException("job, EmbedServer bizThreadPool is EXHAUSTED!");
                    });


            try {
                // start server
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel channel) throws Exception {
                                channel.pipeline()
                                        .addLast(new IdleStateHandler(0, 0, 30 * 3, TimeUnit.SECONDS))  // beat 3N, close if idle
                                        .addLast(new HttpServerCodec())
                                        .addLast(new HttpObjectAggregator(5 * 1024 * 1024))  // merge request & reponse to FULL
                                        .addLast(new EmbedHttpServerHandler(accessToken, bizThreadPool, serverClient));
                            }
                        })
                        .childOption(ChannelOption.SO_KEEPALIVE, true);

                // bind
                ChannelFuture future = bootstrap.bind(port).sync();

                logger.info("job remoting server start success, nettype = {}, port = {}", EmbedServer.class, port);

                // start registry
                startRegistry(appName, address);

                // wait util stop
                future.channel().closeFuture().sync();

            } catch (InterruptedException e) {
                if (e instanceof InterruptedException) {
                    logger.info("job remoting server stop.");
                } else {
                    logger.error("job remoting server error.", e);
                }
            } finally {
                // stop
                try {
                    workerGroup.shutdownGracefully();
                    bossGroup.shutdownGracefully();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }

        });
        thread.setDaemon(true);	// daemon, service jvm, user thread leave >>> daemon leave >>> jvm leave
        thread.start();
    }

    public void stop() throws Exception {
        // destroy server thread
        if (thread!=null && thread.isAlive()) {
            thread.interrupt();
        }

        // stop registry
        stopRegistry();
        logger.info("job remoting server destroy success.");
    }


    // ---------------------- registry ----------------------

    /**
     * netty_http
     */
    public static class EmbedHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
        private static final Logger logger = LoggerFactory.getLogger(EmbedHttpServerHandler.class);

        private ServerClient serverClient;
        private String accessToken;
        private ThreadPoolExecutor threadPoolExecutor;

        public EmbedHttpServerHandler(String accessToken, ThreadPoolExecutor threadPoolExecutor, ServerClient serverClient) {
            this.serverClient = serverClient;
            this.accessToken = accessToken;
            this.threadPoolExecutor = threadPoolExecutor;
        }

        @Override
        protected void channelRead0(final ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {

            // request parse
            String requestData = msg.content().toString(CharsetUtil.UTF_8);
            String uri = msg.uri();
            HttpMethod httpMethod = msg.method();
            boolean keepAlive = HttpUtil.isKeepAlive(msg);
            String accessTokenReq = msg.headers().get(JobRemotingUtil.JOB_ACCESS_TOKEN);

            // invoke
            threadPoolExecutor.execute(() -> {
                // do invoke
                Object responseObj = process(httpMethod, uri, requestData, accessTokenReq);

                // to json
                String responseJson = GsonTool.toJson(responseObj);

                // write response
                writeResponse(ctx, keepAlive, responseJson);
            });
        }

        private Object process(HttpMethod httpMethod, String uri, String requestData, String accessTokenReq) {

            // valid
            if (HttpMethod.POST != httpMethod) {
                return new ReturnT<String>(ReturnT.FAIL_CODE, "invalid request, HttpMethod not support.");
            }
            if (uri==null || uri.trim().length() == 0 ) {
                return new ReturnT<String>(ReturnT.FAIL_CODE, "invalid request, uri-mapping empty.");
            }
            if (accessToken!=null
                    && accessToken.trim().length() > 0
                    && !accessToken.equals(accessTokenReq)) {
                return new ReturnT<String>(ReturnT.FAIL_CODE, "The access token is wrong.");
            }

            // services mapping
            try {
                if ("/beat".equals(uri)) {
                    return serverClient.beat();
                } else if ("/idleBeat".equals(uri)) {
                    IdleBeatParam idleBeatParam = GsonTool.fromJson(requestData, IdleBeatParam.class);
                    return serverClient.idleBeat(idleBeatParam);
                } else if ("/run".equals(uri)) {
                    TriggerParam triggerParam = GsonTool.fromJson(requestData, TriggerParam.class);
                    return serverClient.run(triggerParam);
                } else if ("/kill".equals(uri)) {
                    KillParam killParam = GsonTool.fromJson(requestData, KillParam.class);
                    return serverClient.kill(killParam);
                } else if ("/add".equals(uri)) {
                    TriggerParam triggerParam = GsonTool.fromJson(requestData, TriggerParam.class);
                    return serverClient.add(triggerParam);
                }  else if ("/update".equals(uri)) {
                    TriggerParam triggerParam = GsonTool.fromJson(requestData, TriggerParam.class);
                    return serverClient.update(triggerParam);
                } else {
                    return new ReturnT<String>(ReturnT.FAIL_CODE, "invalid request, uri-mapping("+ uri +") not found.");
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return new ReturnT<String>(ReturnT.FAIL_CODE, "request error:" + ThrowableUtil.toString(e));
            }
        }

        /**
         * write response
         */
        private void writeResponse(ChannelHandlerContext ctx, boolean keepAlive, String responseJson) {
            // write response
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(responseJson, CharsetUtil.UTF_8));   //  Unpooled.wrappedBuffer(responseJson)
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");       // HttpHeaderValues.TEXT_PLAIN.toString()
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
            if (keepAlive) {
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }
            ctx.writeAndFlush(response);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            logger.error("job provider netty_http server caught exception", cause);
            ctx.close();
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                ctx.channel().close();      // beat 3N, close if idle
                logger.debug("job provider netty_http server close an idle channel.");
            } else {
                super.userEventTriggered(ctx, evt);
            }
        }
    }

    // ---------------------- registry ----------------------

    public void startRegistry(final String appName, final String address) {
        // start registry
//        ExecutorRegistryThread.getInstance().start(appName, address);
    }

    public void stopRegistry() {
        // stop registry
//        ExecutorRegistryThread.getInstance().toStop();
    }


}
