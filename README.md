# 智能盒子——基于IOC的智能容器框架

智能盒子是一个容器框架，基于IOC思想，可智能管理项目中的对象（bean），包括加载、实例化、装配、销毁。
使用SmartBox后，你将不用在程序到处new对象，避免硬编码，方便解耦。

（诞生历程：仿照spring就完事了）

### 使用：
#### 1.导入jar包
#### 2.配置文件，说明要管理的bean
#### 3.实例化一个Box对象。
#### 4.从box容器中获取对象


### 细节：
#### 1.导入jar包，仅一个：
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;SmartBox-1.x.x-xxx.jar

(可从download文件夹下找到)

#### 2.配置文件，指定必须是box.xml（目前仅支持xml文件配置），内容如下

##### 1. 简单配置：

    <beans>
        <bean id="userDao" class="com.dao.Impl.UserDaoImpl"></bean>
        <bean id="userService" class="com.service.Impl.UserServiceImpl"></bean>
    </beans>
    
##### 2. 可为bean添加依赖关系，box将会自动帮你在实例化bean的时候注入值

    <beans>
        <bean id="userDao" class="com.dao.Impl.UserDaoImpl"></bean>
        <bean id="userService" class="com.service.Impl.UserServiceImpl">
            <property name="userDao" ref="userDao"></property>
        </bean>
    </beans>
    
<br>&nbsp;&nbsp;&nbsp;&nbsp;    注意：目前box.xml的位置仅支持在maven项目的resource目录下

#### 3.实例化容器Box
<br>&nbsp;&nbsp;&nbsp;&nbsp;   目前仅实现了一个容器：SimpleBox,所以有选择困难症的朋友们有福了
<br>&nbsp;&nbsp;&nbsp;&nbsp;   实例化box只有一句话：
    
    SimpleBox box = SimpleBox.instanceForXml("box.xml");
    
#### 4.从box容器中获取对象
<br>&nbsp;&nbsp;&nbsp;&nbsp;    只有一句话：

        UserService userService = (UserService) simpleBox.getBean("userService");
    
OK，仅需四步，就能彻底远离new，再简单不过了！


后面将会增加依赖注入（DI）功能，以及基于注解的bean管理，彻底告别配置文件