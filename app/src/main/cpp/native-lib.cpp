#include <jni.h>
#include <string>

extern "C"
jstring
Java_com_example_project_1mobile_1application_Model_DisplayAccountsActivity_baseUrlFromJNI(JNIEnv *env, jclass clazz)
{
    std::string baseURL = "https://60102f166c21e10017050128.mockapi.io/labbbank";
    return env->NewStringUTF(baseURL.c_str());
}