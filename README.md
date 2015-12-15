## Build and execution

Generate executable

	mvn clean package

Run with

	java -jar target/operacion.jar

## Health checks

*GET*

	http://localhost:8089/

*GET*

	http://localhost:8089/ping

## Execute

*POST*

	http://localhost:8089/
	
JSON request
	
	{
	    
	    "expression":"100*34"
	}
	
response:

	{"valid":true,"expression":"100*34","result":"3400.00"}