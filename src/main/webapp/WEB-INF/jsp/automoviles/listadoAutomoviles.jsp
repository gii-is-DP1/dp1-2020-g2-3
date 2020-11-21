<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="automoviles">
    <h2>Autom�viles</h2>

    <table id="automovilesTable" class="table table-striped">
        <thead>
        <tr>
         	<th style="width: 120px">Trabajador</th>
            <th style="width: 150px;">Marca</th>
            <th style="width: 200px;">Modelo</th>
            <th style="width: 120px">N�mero de Plazas</th>
            <th style="width: 120px">Kil�metros Recorridos</th>            
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${automoviles}" var="automovil">
            <tr>
             	<td>
                    <c:out value="${automovil.trabajador.nombre}"/>
                    <p> </p>
                     <c:out value="${automovil.trabajador.apellidos}"/>
                     <p> </p>
                     <c:out value="${automovil.trabajador.DNI}"/>
                </td>
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
              </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>