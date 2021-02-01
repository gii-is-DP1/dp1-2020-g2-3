
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="selectAuto-reserva-form">
    
   <h2>Seleccione el automóvil con el que realizará la reserva</h2>
    <form:form  class="form-horizontal" id="select-auto-reservaForm">
        
        
         <table class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Marca</th>
            <th style="width: 200px;">Modelo</th>
            <th style="width: 120px">Número de Plazas</th>
            <th style="width: 120px">Kilómetros Recorridos</th>
            <th style="width: 120px">Seleccionar</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${automoviles}" var="automovil">
            <tr>
             
                <td>
                
                	<c:out value= "${automovil.marca}"/>
                   
                </td>
                <td>
                    <c:out value="${automovil.modelo}"/>
                </td>
                <td>
                    <c:out value="${automovil.numPlazas}"/>
                </td>
                <td>
                    <c:out value="${automovil.kmRecorridos}"/>
                </td>
                <td>
                <input type="radio"  name="autoId" value="${automovil.id}" required/>
                </td>
               
              </tr>
        </c:forEach>
        </tbody>
    </table>
     <button class="btn btn-success" type="submit">Asignar automóvil<span class="glyphicon glyphicon-menu-right" aria-hidden="false"></span></button>
     
    </form:form>
   
</petclinic:layout>
