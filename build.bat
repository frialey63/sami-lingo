rem build.bat

rem mvn -Pproduction clean package

docker build -t pjp/sami-lingo .

docker save -o sami-lingo.tar pjp/sami-lingo:latest
rem scp .\sami-lingo.tar paul@192.168.0.40:/tmp
rem docker load -i /tmp/sami-lingo.tar

rem docker run -p 8090:8080 pjp/sami-lingo
