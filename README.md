

## Insights

Parameters not annotated (eg. with `@RequestBody`) cause the problem.

org.apache.catalina.loader.WebappClassLoaderBase.getResources().getContext()

gives access to the context

Set break points in

- `org.springframework.web.bind.WebDataBinder.doBind(MutablePropertyValues)`
- `org.apache.catalina.valves.AccessLogValve.setSuffix(String)`

## Installing

    ln -s ${HOME}/git/spring4shell-poc/spring4shell-war/target/spring4shell-war-0.1.0-SNAPSHOT.war spring4shell.war

## Running

    ./catalina.sh jpda run

    mvn -am -pl spring4shell-war jetty:run


## Classloaders

- `org.jboss.modules.ModuleClassLoader`
  -> does not seem to expose anything
- `org.eclipse.jetty.webapp.WebAppClassLoader`
  -> exposes a context as well


## Disable Tomcat JSP Support

Remove `jsp` / `org.apache.jasper.servlet.JspServlet` from `${TOMCAT_HOME}/conf/web.xml`

## Custom Valve

Add to `${TOMCAT_HOME}/conf/server.xml`

    <Valve className="com.github.marschall.spring4shell.valve.SimpleFilterValue" />
