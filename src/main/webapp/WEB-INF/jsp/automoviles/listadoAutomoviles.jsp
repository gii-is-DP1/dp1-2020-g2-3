<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="automoviles">
    <h2>Listado de Automóviles</h2>

    <table id="automovilesTable" class="table table-striped">
        <thead>
        <tr>
         	<th style="width: 120px">Trabajador</th>
            <th style="width: 150px;">Marca</th>
            <th style="width: 200px;">Modelo</th>
            <th style="width: 120px">Número de Plazas</th>
            <th style="width: 120px">Kilómetros Recorridos</th>
            <th style="width: 120px">Editar</th>
            <th style="width: 120px">Eliminar</th>            
                      
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
                <td>
                <spring:url value="/automoviles/edit/{autoId}" var="autoEditUrl">
                <spring:param name="autoId" value="${automovil.id}"/>
                </spring:url>
                <a class="editAutomovil" href="${fn:escapeXml(autoEditUrl)}"> <img alt="" id="edit" src="/resources/images/edit.png" style="width: 45px"></a>
                </td>
                <td>
                <spring:url value="/automoviles/delete/{autoId}" var="autoDeleteUrl">
                <spring:param name="autoId" value="${automovil.id}"/>
                </spring:url>
				<a class="deleteAutomovil" href="${fn:escapeXml(autoDeleteUrl)}"> <img alt="" id="delete" src="/resources/images/delete.png" style="width: 45px"></a>
              </tr>
        </c:forEach>
        </tbody>
    </table>
    <p>
    	<a href="/automoviles/new" class="btn  btn-success"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span>Añadir automóvil</a>
    </p>
</petclinic:layout>