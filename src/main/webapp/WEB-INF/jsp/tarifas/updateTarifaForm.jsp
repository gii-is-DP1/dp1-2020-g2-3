<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="tarifas">
    <form:form modelAttribute="tarifa" class="form-horizontal" id="add-auto-form">
        
            <petclinic:inputField label="Precio por kilómetro" name="precioPorKm"/>
            <petclinic:inputField label="Porcentaje IVA Repercutido" name="porcentajeIvaRepercutido"/>
            <petclinic:inputField label="Precio de espera por hora" name="precioEsperaPorHora"/>
            
            <label for="tarifa.activado">Activado:</label>
           
 			<tr>
      			<td>True: <form:radiobutton path="activado" value="true"/> <br/>
          		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; False: <form:radiobutton path="activado" value="false"/> <br/></td>
           
	 		 </tr>
	 		 <br> <br>
	 		
	 		

             <input type="hidden" name="id" id="id" value="${tarifa.id}"/>
             <c:choose>
                    <c:when test="${tarifa['new']}">
                        <button class="btn btn-default" type="submit">Añadir tarifa</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Actualizar tarifa</button>
                    </c:otherwise>
                </c:choose>
    </form:form>
</petclinic:layout>