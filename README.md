
# **LOAN SERVICE INTERVIEW - JAVA 17 - APACHE MAVEN - POSTGRES DATABASE**

---
## PROJECT IMPLEMENTATION SUMMARY

There is one branch: **main**:

- Main branch is implemented to use User, Loan & Transaction entities. Users can onboard as regular users
  or admin users. Regular users can create loans and make payments on loans, but only admins can update
  the status of a loan at fixed intervals of 60 seconds.


---
## SETUP

### 1. LOCAL
- Start Postgres DB via Docker with command below:
```
docker run -d \
  --name my_postgres_container \
  -e POSTGRES_DB=loan_service_interview \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -v /Users/faithfulolaleru/Documents/Volumes:/var/lib/postgresql/data:rw \
  -p 5433:5432 \
  postgres:latest
```
Don't forget to change faithfulolaleru to your username. I put it in your user documents so its easier to track
your data on your local machine

- Create the database on postgres with the following command:
``` 
docker exec -it my_postgres_container psql -U postgres 
```
then:
``` 
CREATE DATABASE loan_service_interview;
```


- Run `mvn spring-boot:run` in terminal. Service starts on port 8080


---
## BUILD PROCESS / IMPROVEMENTS

- Use loan template, where each loan type has preset interest rate

- You can track loan payments on loan entity, and switch status when
  payment is complete i.e on each transaction payment for a loan, reduce
  outstanding payments till zero. This can also help with surplus, if payment 
  is more than the loan

- So you'd ideally want to track base loan, total payable after interest, outstanding 
  unpaid, etc

- Use an interceptor if rate limiting would be application wide

- You should also rate limit for each IP, not for everyone, or better still just use your api gateway to avoid the overhead

- For the test, you should add more failure cases


---

## EXTRAS

- I added my postman collection
- I copied this code from my previous code, and edited it, so no commit history. The previous code 
  repo is: https://github.com/ionknowmyname/musala-soft-interview-3



