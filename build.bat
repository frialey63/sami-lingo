rem build.bat

mvm -Pproduction clean package

docker build -t pjp/sami-lingo-docker .

docker run -p 8080:8080 pjp/sami-lingo-docker