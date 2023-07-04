javac -d frame_work/src frame_work/src/*.java

cd frame_work\src

jar -cf fw.jar .
 
copy fw.jar "..\..\test_frame_work\WEB-INF\lib"

cd ..\..\test_frame_work

jar -cf test.war .

copy test.war "E:\z_mael\server\Tomcat\webapps"