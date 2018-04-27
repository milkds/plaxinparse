public class SearchData {

    private String yearText;
    private String yearValue;
    private String makeText;
    private String makeValue;
    private String modelText;
    private String modelValue;



    private String submodelText;
    private String submodelValue;













    public String getSubmodelText() {
        return submodelText;
    }

    public void setSubmodelText(String submodelText) {
        this.submodelText = submodelText;
    }

    public String getSubmodelValue() {
        return submodelValue;
    }

    public void setSubmodelValue(String submodelValue) {
        this.submodelValue = submodelValue;
    }



    public String getModelText() {
        return modelText;
    }

    public void setModelText(String modelText) {
        this.modelText = modelText;
    }

    public String getModelValue() {
        return modelValue;
    }

    public void setModelValue(String modelValue) {
        this.modelValue = modelValue;
    }

    public String getYearText() {
        return yearText;
    }

    public void setYearText(String yearText) {
        this.yearText = yearText;
    }

    public String getYearValue() {
        return yearValue;
    }

    public void setYearValue(String yearValue) {
        this.yearValue = yearValue;
    }

    public String getMakeText() {
        return makeText;
    }

    public void setMakeText(String makeText) {
        this.makeText = makeText;
    }

    public String getMakeValue() {
        return makeValue;
    }

    public void setMakeValue(String makeValue) {
        this.makeValue = makeValue;
    }

    @Override
    public String toString() {
        return "SearchData{" +
                "yearText='" + yearText + '\'' +
                ", makeText='" + makeText + '\'' +
                ", modelText='" + modelText + '\'' +
                ", submodelText='" + submodelText + '\'' +
                '}';
    }
}
