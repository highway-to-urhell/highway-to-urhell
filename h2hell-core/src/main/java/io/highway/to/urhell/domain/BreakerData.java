package io.highway.to.urhell.domain;

public class BreakerData {

    private String className;
    private String classNameNormalized;
    private String methodName;
    private String signatureName;

    public String getSignatureName() {
        return signatureName;
    }

    public void setSignatureName(String signatureName) {
        this.signatureName = signatureName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassNameNormalized() {
        return classNameNormalized;
    }

    public void setClassNameNormalized(String classNameNormalized) {
        this.classNameNormalized = classNameNormalized;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return "BreakerData [className=" + className + ", classNameNormalized="
                + classNameNormalized + ", methodName=" + methodName
                + ", signatureName=" + signatureName + "]";
    }


}
