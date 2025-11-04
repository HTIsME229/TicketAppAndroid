package com.example.ticketapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ticketapp.domain.model.AuthResult;
import com.example.ticketapp.data.repository.AccountRepository;


import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProfileViewModel extends ViewModel {
    private final AccountRepository accountRepository;


    @Inject
    public ProfileViewModel(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public  LiveData<AuthResult> login(String email, String password){
        return accountRepository.login(email,password);
    }
    public LiveData<AuthResult>  register(String name, String email, String password){
return         accountRepository.register(name, email, password);




    }
//    public  LiveData<Account> getAccount(){
//        return accountRepository.();
//    }
    public  void logout(){
        accountRepository.logout();
    }
    public  Boolean isUserLoggedIn(){
        return accountRepository.isUserLoggedIn();
    }
public LiveData<Boolean> observerAuthState(){
    return accountRepository.observeAuthState();}

}
