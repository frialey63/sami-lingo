rem build.bat

docker build -t pjp/sami-lingo-docker .

docker run -p 8080:8080 pjp/sami-lingo-docker