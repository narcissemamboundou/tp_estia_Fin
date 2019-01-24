<!doctype html>
<html lang="en" class="no-js">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>
        <g:layoutTitle default="Grails"/>
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico" />

    <asset:stylesheet src="application.css"/>

    <g:layoutHead/>
</head>
<body>

    <div class="navbar navbar-default navbar-static-top" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="/#">
		    <asset:image src="logo-estia.png" alt="Estia Logo"/>
                </a>
            </div>
            <nav class="nav">
                <g:if test="${params.controller == 'user'}"><a class="nav-link active" href="${g.createLink(controller: 'user')}">Users</a></g:if>
                <g:else><a class="nav-link" href="${g.createLink(controller: 'user')}">Users</a></g:else>
                <a class="nav-link <g:if test="${params.controller == 'message'}">nav-link active</g:if>" href="${g.createLink(controller: 'message')}">Messages</a>
                <a class="nav-link <g:if test="${params.controller == 'role'}">nav-link active</g:if>" href="${g.createLink(controller: 'role')}">Groupes</a>
            </nav>
        </div>
    </div>

    <g:layoutBody/>

    <div class="footer" role="contentinfo"></div>

    <div id="spinner" class="spinner" style="display:none;">
        <g:message code="spinner.alt" default="Loading&hellip;"/>
    </div>

    <asset:javascript src="application.js"/>

</body>
</html>
