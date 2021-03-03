package designpattern.proxy;

public class Client {


    public static void main(String[] args) {
        AgentHandler agentHandler = new AgentHandler(new KunKun());
        Start start = (Start) agentHandler.create();
        start.singe();
    }

}
