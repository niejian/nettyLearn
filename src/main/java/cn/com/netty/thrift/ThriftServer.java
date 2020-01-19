package cn.com.netty.thrift;

import cn.com.netty.thrift.generate.PersonService;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.TTransportFactory;

/**
 * @user niejian9001@163.com
 * @date 2020/1/19 20:35
 */
public class ThriftServer {
    public static void main(String[] args) throws TTransportException {
        TNonblockingServerSocket server = new TNonblockingServerSocket(8899);
        THsHaServer.Args arg = new THsHaServer.Args(server).minWorkerThreads(2).maxWorkerThreads(4);
        PersonService.Processor<PersonServiceImpl> processor = new PersonService.Processor<>(new PersonServiceImpl());

        // 协议层
        arg.protocolFactory(new TCompactProtocol.Factory());
        // 传输层对象
        arg.transportFactory(new TFramedTransport.Factory());
        arg.processorFactory(new TProcessorFactory(processor));
        TServer tServer = new THsHaServer(arg);
        System.out.println("thrift server started!!!");
        tServer.serve();
    }
}
