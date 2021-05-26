package app.domain.model;

import java.util.List;

public class TestParam {
    List<Parameter> parametersSelected;
    public TestParam(Test test) {
        this.parametersSelected = test.getParameter();
    }

    public TestParam() {

    }

    public Parameter findParameterInTestParameter(String parameterCode) {
        for (Parameter parameter : parametersSelected) {
            if (parameter.getCode().equals(parameterCode)) {
                return parameter;
            }
        }
        return null; // not found
    }

}
