init-local:
	@docker compose up -d
	@sleep 5
	@mc alias set local http://localhost:9000 minioaccesskey miniosecretkey
	@mc mb local/test-bucket

run-local:
	@mvn clean spring-boot:run \
		-Dspring-boot.run.profiles=local \
		-Dspring.output.ansi.enabled=always

s3-ls-local:
	@mc ls local/test-bucket

s3-stat-local:
	@mc stat local/test-bucket/35359f60-19da-40df-a7e8-e727be078b7a