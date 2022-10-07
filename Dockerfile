FROM jboss/wildfly:13.0.0.Final

RUN /opt/jboss/wildfly/bin/add-user.sh adm adm1 --silent