mvn versions:set -DnewVersion=0.0.2

mvn clean install -N
mvn clean install -pl jedi-core

mvn clean install -pl jedi-core -am
mvn clean install -pl jedi-client -am


mvn clean package -pl jedi-admin -am
mvn clean package -pl jedi-config -am
mvn clean package -pl jedi-consumer -am

mvn clean deploy -pl jedi-client -am -Dmaven.test.skip=true -U -B