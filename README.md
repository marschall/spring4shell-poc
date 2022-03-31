

## Insights

Parameters not annotated (eg. with `@RequestBody`) cause the problem.

org.apache.catalina.loader.WebappClassLoaderBase.getResources().getContext()

gives access to the context

## Installing

    ln -s ${HOME}/git/spring4shell-poc/spring4shell-war/target/spring4shell-war-0.1.0-SNAPSHOT.war spring4shell.war

## Running

    ./catalina.sh jpda debug