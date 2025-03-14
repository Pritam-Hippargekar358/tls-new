package org.root.keycloak;

import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
import java.util.Objects;

public class TestKeyCloack {

    public static void main(String[] args) {
        String realmName = "ayushman-realm1";
        TestKeyCloack test = new TestKeyCloack();
//        AccessTokenResponse tokenResponse = getInstance().tokenManager().getAccessToken();
//        System.out.println(tokenResponse.getToken());

        test.createUser(realmName);

//        boolean status = test.deleteUser("6517c6a5-a03e-425c-8af5-057935748b4d");
//        System.out.println(status);
    }

    public Keycloak getInstance(){
            return KeycloakBuilder.builder() //
                    .serverUrl("http://localhost:8080") //
                    .realm("ayushman-realm1") //
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS) //
                    .clientId("ayushman-client1") //
                    .clientSecret("lPnVaPH5SqaJRo19FusQsxke6WnpYJrF") //
                    .build();
    }

    public void createUser(String realmName) {
        UserRepresentation userRepresentation = this.userRepresentationDtl();
        Response response = usersResourceDtl(realmName).create(userRepresentation);
        System.out.println("response code:"+ response.getStatus());
        System.out.println("response code:"+ response);
        if(Objects.equals(201,response.getStatus())){
            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            System.out.println("User created with userId:"+ userId);
            this.assignRoleToUser(userId, "ADMIN", realmName);

//            userRepresentation = usersResourceDtl().search("abc@example.com").getFirst();
//            UserResource userResource = usersResourceDtl().get(userRepresentation.getId());
//            RoleRepresentation roleRepresentation = rolesResourceDtl().get("ADMIN").toRepresentation();
//            System.out.println("roleId:"+ roleRepresentation.getId());
//            List<RoleRepresentation> rolesToAdd = List.of(roleRepresentation);
//            userResource.roles().realmLevel().add(rolesToAdd);

        }
    }

    public void assignRoleToUser(String userId, String roleName, String realmName) {
        UserResource userResource = this.getUser(userId, realmName);
        RoleRepresentation roleRepresentation = rolesResourceDtl(realmName).get(roleName).toRepresentation();
        System.out.println("roleId:"+ roleRepresentation.getId());
        userResource.roles().realmLevel().add(List.of(roleRepresentation));
    }

    public UserRepresentation userRepresentationDtl() {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEmail("abc@example.com");
        userRepresentation.setUsername("abc@example.com");
        userRepresentation.setFirstName("abc");
        userRepresentation.setLastName("xyz");
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);
        CredentialRepresentation userCredentialRepresentation = this.createPasswordCredentials("Pass@1234");
        userRepresentation.setCredentials(List.of(userCredentialRepresentation));
        return userRepresentation;
    }

    public CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

    public UsersResource usersResourceDtl(String realmName){
        return realmResourceDtl(realmName).users();
    }

    public RolesResource rolesResourceDtl(String realmName){
        return realmResourceDtl(realmName).roles();
    }

    public RealmResource realmResourceDtl(String realmName){
        return getInstance().realm(realmName);
    }

    public UserResource getUser(String userId, String realmName) {
        return usersResourceDtl(realmName).get(userId);
    }

    public boolean deleteUser(String userId, String realmName) {
        try (Response response = usersResourceDtl(realmName).delete(userId)) {
            System.out.println("response code:"+ response.getStatus());
            if (Objects.equals(204, response.getStatus())) {
               return true;
            }
            return false;
        }
    }

    public void deleteRoleFromUser(String userId, String roleName, String realmName) {
        UserResource userResource = this.getUser(userId, realmName);
        RoleRepresentation roleRepresentation = rolesResourceDtl(realmName).get(roleName).toRepresentation();
        userResource.roles().realmLevel().remove(List.of(roleRepresentation));
    }

}
