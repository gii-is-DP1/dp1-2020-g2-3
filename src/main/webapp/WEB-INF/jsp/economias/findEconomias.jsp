<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="economias">

	<jsp:body>
    <h2>Calcular Ingresos y gastos</h2>

    <form:form modelAttribute="economia" action="/economias/calcular" method="get" class="form-horizontal"
               id="search-owner-form">
        <div class="form-group">
            <div class="form-group has-feedback">
            <label for="fechaInicio">Fecha inicial</label> 
			<input type="date" name="fecha1"  id="fecha1" value="<fmt:formatDate pattern = "yyyy-MM-dd" value = "${economia.fecha1}" />"/> 
			<br>
			<label for="fechaFinal">Fecha final</label> 
			<input type="date" name="fecha2"  id="fecha2" value="<fmt:formatDate pattern = "yyyy-MM-dd" value = "${economia.fecha2}" />"/> 
       		
       		</div>

        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-default">Calcular</button>
            </div>
        </div>

    </form:form>
	</jsp:body>
	
</petclinic:layout>