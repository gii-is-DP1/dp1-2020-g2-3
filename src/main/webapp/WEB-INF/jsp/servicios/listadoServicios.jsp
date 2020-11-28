<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="servicios">
    <h2>Servicios</h2>

    <table id="serviciosTable" class="table table-striped">
        <thead>
        <tr>
            <th>Nombre</th>
            <th>Fecha</th>
            <th>Precio</th>
            <th>Trabajador</th>
            <th>Automóvil</th>
            <th>Taller</th>
            <th>Descripción</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${servicios}" var="servicio">
            <tr>
              <td>
                    <c:out value="${servicio.name}"/>
                </td>
                <td>
                    <c:out value="${servicio.fecha}"/>
                </td>
                <td>
                    <c:out value="${servicio.precio}"/>
                </td>
               <td>
                    <c:out value="${servicio.trabajador.nombre}"/>
                    <p> </p>
                     <c:out value="${servicio.trabajador.apellidos}"/>
                     <p> </p>
                     <c:out value="${servicio.trabajador.DNI}"/>
                </td>
                <td>
                    <c:out value="${servicio.automovil.id}"/>
                </td>
                <td>
                    <c:out value="${servicio.taller.name}"/>
                </td>
                <td>
                    <c:out value="${servicio.descripcion}"/>
                </td>
                <td>
                <spring:url value="/servicios/edit/{servicioId}" var="servicioEditUrl">
                <spring:param name="servicioId" value="${servicio.id}"/>
                </spring:url>
                <a class="editServicio" href="${fn:escapeXml(servicioEditUrl)}"> <img alt="" id="edit" src="/resources/images/edit.png" style="width: 45px"></a>
                </td>
                <td>
                <spring:url value="/servicios/delete/{servicioId}" var="servicioDeleteUrl">
                <spring:param name="servicioId" value="${servicio.id}"/>
                </spring:url>
				<a class="deleteServicio" href="${fn:escapeXml(servicioDeleteUrl)}"> <img alt="" id="delete" src="/resources/images/delete.png" style="width: 45px"></a>
              </tr>
        </c:forEach>
        </tbody>
    </table>
    <p>
    	<a href="/servicios/new" class="btn  btn-success"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span>Añadir Servicio</a>
    </p>
</petclinic:layout>
