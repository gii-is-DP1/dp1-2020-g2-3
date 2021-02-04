<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="economias">

	<jsp:body>
    <h2>Mostrar Ingresos y gastos</h2>

        <div class="form-group">
            <div class="form-group has-feedback">
       		<h2>
       		<c:if test="${not empty ingresos}"> Los ingresos en dicho rango de fechas han sido de -> <c:out value="${ingresos}"/></c:if>
       		</h2>
       		
       		<h2>
       		<c:if test="${not empty gastos}"> Los gastos en dicho rango de fechas han sido de -> <c:out value="${gastos}"/></c:if>
       		</h2>
       		
       		</div>

        </div>

	</jsp:body>
	
</petclinic:layout>