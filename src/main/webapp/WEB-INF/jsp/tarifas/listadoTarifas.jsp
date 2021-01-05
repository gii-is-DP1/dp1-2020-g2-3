<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="tarifas">
    <h2>Tarifas</h2>

    <table id="tarifasTable" class="table table-striped">
        <thead>
        <tr>
            <th>Precio por kil�metro</th>
            <th>Porcentaje IVA Repercutido</th>
            <th>Precio de espera por hora</th>
            <th>Activado</th>
            <th>Editar</th>
            <th>Eliminar</th>   
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${tarifas}" var="tarifa">
            <tr>
              <td>
                    <c:out value="${tarifa.precioPorKm}"/>
                </td>
                <td>
                    <c:out value="${tarifa.porcentajeIvaRepercutido}"/>
                </td>
                <td>
                    <c:out value="${tarifa.precioEsperaPorHora}"/>
                </td>
               <td>
                    <c:out value="${tarifa.activado}"/>
                </td>
                <td>
                <spring:url value="/tarifas/edit/{tarifaId}" var="tarifaEditUrl">
                <spring:param name="tarifaId" value="${tarifa.id}"/>
                </spring:url>
                <a class="editTarifa" href="${fn:escapeXml(tarifaEditUrl)}"> <img alt="" id="edit" src="/resources/images/edit.png" style="width: 45px"></a>
                </td>
                <td>
                <spring:url value="/tarifas/delete/{tarifaId}" var="tarifaDeleteUrl">
                <spring:param name="tarifaId" value="${tarifa.id}"/>
                </spring:url>
				<a class="deleteTarifa" href="${fn:escapeXml(tarifaDeleteUrl)}"> <img alt="" id="delete" src="/resources/images/delete.png" style="width: 45px"></a>
              </tr>
        </c:forEach>
        </tbody>
    </table>
    <p>
    	<a href="/tarifas/new" class="btn  btn-success"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span>A�adir Tarifa</a>
    </p>
</petclinic:layout>
