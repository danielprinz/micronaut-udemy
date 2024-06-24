package com.danielprinz.udemy.news;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import java.time.Month;

@Controller
@ExecuteOn(TaskExecutors.BLOCKING)
public class NewsController {

  private final NewsService newsService;

  public NewsController(NewsService newsService) {
    this.newsService = newsService;
  }

  @Get("/headline/{month}")
  public Headline getHeadline(@PathVariable Month month) {
    return new Headline(month, newsService.headlines(month));
  }
}
