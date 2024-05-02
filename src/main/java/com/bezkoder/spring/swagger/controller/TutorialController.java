package com.bezkoder.spring.swagger.controller;

import com.bezkoder.spring.swagger.model.Tutorial;
import com.bezkoder.spring.swagger.service.TutorialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Tutorial", description = "Tutorial management APIs")
@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
@Log4j2
public class TutorialController {
  private final TutorialService tutorialService;

  @Operation(summary = "Create a new Tutorial")
  @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = Tutorial.class), mediaType = "application/json")},
      headers = {@Header(name = "clientMessageId", description = "Using trace log", schema = @Schema(type = "String", example = "1e2ae806-413a-454d-9908-caa399e79077"))})
  @PostMapping("/tutorials")
  public ResponseEntity<Tutorial> createTutorial(@RequestHeader String clientMessageId, @RequestHeader(required = false) String transactionId, @RequestBody Tutorial tutorial) {
    try {
      var createTutorial = tutorialService.save(new Tutorial(tutorial.getTitle(), tutorial.getDescription(), tutorial.isPublished()));
      return new ResponseEntity<>(createTutorial, HttpStatus.CREATED);
    } catch (Exception e) {
      log.error("create tutorial error", e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Operation(summary = "Retrieve all Tutorials")
  @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = Tutorial.class)), mediaType = "application/json")},
      headers = {@Header(name = "clientMessageId", description = "Using trace log", schema = @Schema(type = "String", example = "1e2ae806-413a-454d-9908-caa399e79077"))})
  @GetMapping("/tutorials")
  public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestHeader String clientMessageId, @RequestParam(required = false) String title) {
    try {
      var tutorials = new ArrayList<Tutorial>();
      if (title == null) {
        tutorials.addAll(tutorialService.findAll());
      } else {
        tutorials.addAll(tutorialService.findByTitleContaining(title));
      }
      if (tutorials.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(tutorials, HttpStatus.OK);
    } catch (Exception e) {
      log.error("get tutorial error", e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Operation(summary = "Retrieve a Tutorial by Id", description = "Get a Tutorial object by specifying its id. The response is Tutorial object with id, title, description and published status.")
  @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Tutorial.class), mediaType = "application/json")},
      headers = {@Header(name = "clientMessageId", description = "Using trace log", schema = @Schema(type = "String", example = "1e2ae806-413a-454d-9908-caa399e79077"))})
  @GetMapping("/tutorials/{id}")
  public ResponseEntity<Tutorial> getTutorialById(@RequestHeader String clientMessageId, @PathVariable("id") long id) {
    var tutorial = tutorialService.findById(id);
    if (tutorial != null) {
      return new ResponseEntity<>(tutorial, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Operation(summary = "Update a Tutorial by Id")
  @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Tutorial.class), mediaType = "application/json")},
      headers = {@Header(name = "clientMessageId", description = "Using trace log", schema = @Schema(type = "String", example = "1e2ae806-413a-454d-9908-caa399e79077"))})
  @PutMapping("/tutorials/{id}")
  public ResponseEntity<Tutorial> updateTutorial(@RequestHeader String clientMessageId, @PathVariable("id") long id, @RequestBody Tutorial tutorial) {
    var updateTutorial = tutorialService.findById(id);
    if (updateTutorial != null) {
      updateTutorial.setTitle(tutorial.getTitle());
      updateTutorial.setDescription(tutorial.getDescription());
      updateTutorial.setPublished(tutorial.isPublished());
      return new ResponseEntity<>(tutorialService.save(updateTutorial), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Operation(summary = "Delete a Tutorial by Id")
  @ApiResponse(responseCode = "204",
      headers = {@Header(name = "clientMessageId", description = "Using trace log", schema = @Schema(type = "String", example = "1e2ae806-413a-454d-9908-caa399e79077"))})
  @DeleteMapping("/tutorials/{id}")
  public ResponseEntity<HttpStatus> deleteTutorial(@RequestHeader String clientMessageId, @PathVariable("id") long id) {
    try {
      tutorialService.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      log.error("delete error", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Operation(summary = "Delete all Tutorials")
  @ApiResponse(responseCode = "204",
      headers = {@Header(name = "clientMessageId", description = "Using trace log", schema = @Schema(type = "String", example = "1e2ae806-413a-454d-9908-caa399e79077"))})
  @DeleteMapping("/tutorials")
  public ResponseEntity<HttpStatus> deleteAllTutorials(@RequestHeader String clientMessageId) {
    try {
      tutorialService.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      log.error("delete error", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Operation(summary = "Retrieve all published Tutorials")
  @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = Tutorial.class)), mediaType = "application/json")},
      headers = {@Header(name = "clientMessageId", description = "Using trace log", schema = @Schema(type = "String", example = "1e2ae806-413a-454d-9908-caa399e79077"))})
  @ApiResponse(responseCode = "204",
      headers = {@Header(name = "clientMessageId", description = "Using trace log", schema = @Schema(type = "String", example = "1e2ae806-413a-454d-9908-caa399e79077"))})
  @GetMapping("/tutorials/published")
  public ResponseEntity<List<Tutorial>> findByPublished(@RequestHeader String clientMessageId) {
    try {
      List<Tutorial> tutorials = tutorialService.findByPublished(true);
      if (tutorials.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(tutorials, HttpStatus.OK);
    } catch (Exception e) {
      log.error("find error", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
