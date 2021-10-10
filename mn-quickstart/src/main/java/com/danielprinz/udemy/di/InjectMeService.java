package com.danielprinz.udemy.di;

//Injected via Factory
//@Singleton is not needed
class InjectMeService {

  public String className() {
    return getClass().getSimpleName();
  }
}
