<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ attribute name="menuName" required="true" rtexprvalue="true"
              description="Name of the active menu: home, owners, vets or error" %>
              
 
<sec:authorize access="hasAnyAuthority('admin','taxista')"> <petclinic:menu name="${menuName}"/> </sec:authorize>
<sec:authorize access="hasAnyAuthority('cliente') or !isAuthenticated()"> <petclinic:menuClientes name="${menuName}"/> </sec:authorize>