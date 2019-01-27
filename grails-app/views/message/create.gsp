<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'message.label', default: 'Message')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#create-message" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>
        <div id="create-message" class="content scaffold-create" role="main">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>

            </g:if >
            <g:hasErrors bean="${this.message}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.message}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <g:form resource="${this.message}" method="POST">
                <fieldset class="form" >

                    <div class="fieldcontain required">
                        <label for="messageContent">Message Content
                            <span class="required-indicator">*</span>
                        </label>

                        <textarea  name="messageContent" id="messageContent" value="${message.messageContent}  cols="40" rows="10"></textarea>
                    </div>

                    <div class="fieldcontain required">
                        <label for="author">Author
                            <span class="required-indicator">*</span>
                        </label><select name="author.id" required="" id="author">
                        <option value="1">User(username:admin)</option>
                        <option value="2">User(username:user)</option>
                        <option value="3">User(username:username-1)</option>
                        <option value="4">User(username:username-2)</option>
                        <option value="5">User(username:username-3)</option>
                        <option value="6">User(username:username-4)</option>
                        <option value="7">User(username:username-5)</option>
                        <option value="8">User(username:username-6)</option>
                        <option value="9">User(username:username-7)</option>
                        <option value="10">User(username:username-8)</option>
                        <option value="11">User(username:username-9)</option>
                        <option value="12">User(username:username-10)</option>
                        <option value="13">User(username:username-11)</option>
                        <option value="14">User(username:username-12)</option>
                        <option value="15">User(username:username-13)</option>
                        <option value="16">User(username:username-14)</option>
                        <option value="17">User(username:username-15)</option>
                        <option value="18">User(username:username-16)</option>
                        <option value="19">User(username:username-17)</option>
                        <option value="20">User(username:username-18)</option>
                        <option value="21">User(username:username-19)</option>
                        <option value="22">User(username:username-20)</option>
                        <option value="23">User(username:username-21)</option>
                        <option value="24">User(username:username-22)</option>
                        <option value="25">User(username:username-23)</option>
                        <option value="26">User(username:username-24)</option>
                        <option value="27">User(username:username-25)</option>
                        <option value="28">User(username:username-26)</option>
                        <option value="29">User(username:username-27)</option>
                        <option value="30">User(username:username-28)</option>
                        <option value="31">User(username:username-29)</option>
                        <option value="32">User(username:username-30)</option>
                        <option value="33">User(username:username-31)</option>
                        <option value="34">User(username:username-32)</option>
                        <option value="35">User(username:username-33)</option>
                        <option value="36">User(username:username-34)</option>
                        <option value="37">User(username:username-35)</option>
                        <option value="38">User(username:username-36)</option>
                        <option value="39">User(username:username-37)</option>
                        <option value="40">User(username:username-38)</option>
                        <option value="41">User(username:username-39)</option>
                        <option value="42">User(username:username-40)</option>
                        <option value="43">User(username:username-41)</option>
                        <option value="44">User(username:username-42)</option>
                        <option value="45">User(username:username-43)</option>
                        <option value="46">User(username:username-44)</option>
                        <option value="47">User(username:username-45)</option>
                        <option value="48">User(username:username-46)</option>
                        <option value="49">User(username:username-47)</option>
                        <option value="50">User(username:username-48)</option>
                        <option value="51">User(username:username-49)</option>
                        <option value="52">User(username:username-50)</option>
                    </select>
                    </div>



                        <g:submitButton name="Save" value="Save"/>
                        <g:link action="create">Cancel</g:link>




                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
