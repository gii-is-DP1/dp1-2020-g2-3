<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="clientes">
    <h2>Clientes</h2>

    <table id="clientesTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Nombre</th>
            <th style="width: 200px;">DNI</th>
            <th style="width: 120px">Email</th>
            <th style="width: 120px">Telefono</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${selections}" var="cliente">
            <tr>
                <td>
                    <spring:url value="/clientes/{clienteId}" var="clienteUrl">
                        <spring:param name="clienteId" value="${cliente.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(clienteUrl)}"><c:out value="${cliente.firstName} ${cliente.lastName}"/></a>
                </td>
                <td>
                    <c:out value="${cliente.dni}"/>
                </td>
                <td>
                    <c:out value="${cliente.email}"/>
                </td>
                <td>
                    <c:out value="${cliente.telefono}"/>
                </td>
                
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
