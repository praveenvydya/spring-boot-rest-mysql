 
package com.portal.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.portal.dto.UserDetails;
import com.portal.exception.ResourceNotFoundException;
import com.portal.model.User;
import com.portal.repository.UserRepository;
import com.portal.services.UserService;

 
@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserRepository userRepository;
  
  @Autowired
  private UserService userService;
  @Autowired 
  private ModelMapper mapper;

  /**
   * Get all users list.
   *
   * @return the list
   */
  @GetMapping
  public ResponseEntity<Object> getAllUsers() {
   // return userRepository.findAll();
	  //return convertAllToDto(userService.getAll());
	  return new ResponseEntity<>(convertAllToDto(userService.getAll()), HttpStatus.OK);
  }

  /**
   * Gets users by id.
   *
   * @param userId the user id
   * @return the users by id
   * @throws ResourceNotFoundException the resource not found exception
   */
  @GetMapping("/{id}")
  public ResponseEntity<Object>  getUsersById(@PathVariable(value = "id") Long userId)
      throws ResourceNotFoundException {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));
    return ResponseEntity.ok().body(user);
  }

  /**
   * Create user user.
   *
   * @param user the user
   * @return the user
   */
  @PostMapping
  public ResponseEntity<Object>  createUser(@Valid @RequestBody User user) {
	  return new ResponseEntity<>(convertToDto(userRepository.save(user)), HttpStatus.OK);
  }

  /**
   * Update user response entity.
   *
   * @param userId the user id
   * @param userDetails the user details
   * @return the response entity
   * @throws ResourceNotFoundException the resource not found exception
   */
  

  
  @PutMapping("/{id}")
  public ResponseEntity<Object>  updateUser(
      @PathVariable(value = "id") Long userId,@Valid @RequestBody User userDetails)  //  //@Valid
      throws ResourceNotFoundException {

    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));

    user.setEmail(userDetails.getEmail());
    user.setLastName(userDetails.getLastName());
    user.setFirstName(userDetails.getFirstName());
    user.setUpdatedAt(new Date());
    final User updatedUser = userRepository.save(user);
    return ResponseEntity.ok(updatedUser);
  }

  /**
   * Delete user map.
   *
   * @param userId the user id
   * @return the map
   * @throws Exception the exception
   */
  @DeleteMapping("/{id}")
  public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId) throws Exception {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));

    userRepository.delete(user);
    Map<String, Boolean> response = new HashMap<>();
    response.put("deleted", Boolean.TRUE);
    return response;
  }
  
  private UserDetails convertToDto(User user) {
	  UserDetails dto = mapper.map(user, UserDetails.class);
	  
	 // UserDetails dto = new UserDetails();
	  
	  return dto;
  }
  
  private List<UserDetails> convertAllToDto(List<User> users) {
	  List<UserDetails> dtos = new ArrayList<UserDetails>();
	  if(null!=users) {
		  for(User u:users) {
			  dtos.add(convertToDto(u));
		  }
	  }
	  return dtos;
  }
}
