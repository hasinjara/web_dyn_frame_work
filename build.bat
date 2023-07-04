javac -d frame_work/src frame_work/src/*.java

javac -d "test_frame_work\WEB-INF\classes" Model_test/*.java 

cd frame_work\src

jar -cf fw.jar .
 
copy fw.jar "..\..\test_frame_work\WEB-INF\lib"

copy fw.jar "E:\z_mael\etude\class_path"

cd ..\..\test_frame_work

jar -cf test.war .

copy test.war "E:\z_mael\server\Tomcat\webapps"