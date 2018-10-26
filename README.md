# 自动生成dimens.xml jar

## 用法

### 不认参数
```java
java -jar Dimens.jar
```
>默认参数：DP_FROM:-2560.0f,DP_TO:2560.0f,DP_STEP_SIZE=0.5f;
         SP_FROM = 9f,SP_TO = 30f,SP_STEP_SIZE = 1f

### 带参数
```java
java -jar Dimens.jar -100.0 100 0.5 9.0 30.0 1.0
```
>1:DP_FROM,2:DP_TO,3:DP_STEP_SIZE;
4:SP_FROM,5:SP_TO,6:SP_STEP_SIZE(不需要修改的参数可以传非数字：例如"-")