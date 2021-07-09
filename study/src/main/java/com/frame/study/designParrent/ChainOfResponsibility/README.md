###责任链模式
**责任链模式（Chain of Responsibility Pattern）为请求创建了一个接收者对象的链。
这种模式给予请求的类型，对请求的发送者和接收者进行解耦。这种类型的设计模式属于行为型模式。
在这种模式中，通常每个接收者都包含对另一个接收者的引用。如果一个对象不能处理该请求，
那么它会把相同的请求传给下一个接收者，依此类推。[引用w3c对责任链的描述](https://www.runoob.com/design-pattern/chain-of-responsibility-pattern.html)**   
在实际的开发中我们也会遇见，我们需要对外部提供一个接口，中间可能会结果很多非业务的处理方法（日志记录，权限校验，
敏感数据清洗....） 但是对于业务来说是透明的。每个处理器都是独立的，不应该存在耦合关系，才可以让我们随意的去拼接。  
![责任链-01](https://img04.sogoucdn.com/app/a/100520146/ce9ee410725183b6ef51c6699976802b)
这里我们先要创建一个接口，这些处理去都是去实现这个接口的
