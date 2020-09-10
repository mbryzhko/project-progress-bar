mvn docker:start -P integration-tests -Ddocker.it.mysql.port=3306  
mvn docker:stop -P integration-tests
