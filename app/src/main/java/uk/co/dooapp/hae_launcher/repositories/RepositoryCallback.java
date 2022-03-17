package uk.co.dooapp.hae_launcher.repositories;

public interface RepositoryCallback<T> {
    void onComplete(Result<T> result);
}