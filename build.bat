rem build.bat

#mvn -Pproduction clean package

docker build -t pjp/sami-lingo .

docker save -o sami-lingo.tar pjp/sami-lingo:latest
scp .\sami-lingo.tar paul@192.168.0.40:/tmp
#docker load -i /tmp/sami-lingo.tar

#docker run -p 8080:8080 pjp/sami-lingo
