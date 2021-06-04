#### gradle 配置 多模块

##### 1.创建gradle（parent）项目
##### 2.选上parent右键创建model（gradle）
##### 3. 把parent的build.gradle的内容放到allprojects {}，plugins {id("xxxx")}要改成apply plugin:'xxx',然后添加公共依赖,
##### 4. 删除model的build.gradle和parent的重复内容 如果依赖其他 model,可以添加compile project(":model_name")
##### 5.在需要打bootjar包的model中添加plugins{id("org.springframework.boot") version "2.3.2.RELEASE"}
##### 6.修改打包名字和版本号

##### 7. parent build.gradle

```groovy
apply from: 'dependencies.gradle'
allprojects{
    apply plugin: 'java'

    group 'com.ityu'
    version '1.0-SNAPSHOT'

    sourceCompatibility = 1.8

    repositories {
        mavenCentral()
        maven{ url 'https://maven.aliyun.com/repository/public/' }
    }

    dependencies {
        testCompile group: 'junit', name: 'junit', version: '4.12'
        implementation libraries['spring-boot-starter']
        implementation libraries['spring-boot-starter-web']
        implementation libraries['swagger2']
        implementation libraries['swagger-ui']
    }
}

```
##### 7. module-1 build.gradle

```groovy
plugins {
    id "io.freefair.lombok" version "5.3.0"
}
dependencies {
    implementation libraries['web3j']
    implementation libraries['okhttp']
}

```

##### 8. module-2 build.gradle

```groovy
plugins {
    id 'org.springframework.boot' version '2.4.5'
    id "io.freefair.lombok" version "5.3.0"
}

dependencies {
    compile project(":common")
    testImplementation libraries['spring-boot-starter-test']

}


```


###### 9. other
```groovy
rootProject.name = 'blockstudy'
include 'common'
include 'blockApi'

```
