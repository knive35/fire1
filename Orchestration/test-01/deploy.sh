oc config use-context testingjenkinss-test-01-apps
oc create configmap app-config --from-file=src/main/resources/config-test-01/application-dev.yml
oc apply -f Orchestration/test-01/deployment.yaml
oc apply -f Orchestration/test-01/service.yaml