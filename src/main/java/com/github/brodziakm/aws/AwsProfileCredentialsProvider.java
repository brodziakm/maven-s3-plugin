package com.github.brodziakm.aws;

import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkException;

/**
 * {@link AwsCredentialsProvider} for assuming a specified role automatically renewing the temporary credentials as needed.
 */
public class AwsProfileCredentialsProvider implements AwsCredentialsProvider {

  private static final String[] SSO_LOGIN = buildBaseAwsLoginCommand();

  private final Lock lock = new ReentrantLock();

  private final String profile;

  private ProfileCredentialsProvider credentialsProvider;

  private AwsCredentials credentials;

  /**
   * Creates a new provider.
   * 
   * @param profile the name of the profile that should be used by this credentials provider
   */
  public AwsProfileCredentialsProvider(String profile) {
    this.profile = profile;
    rebuildProvider();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AwsCredentials resolveCredentials() {
    try {
      lock.lockInterruptibly();

      if (credentials == null || credentials.expirationTime()
        .filter(expiration -> Instant.now().isBefore(expiration))
        .isEmpty()) {
        doResolveCredentials();
      }
    } catch (SdkException e) {
      tryLoginAndResolve(e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      lock.unlock();
    }

    return credentials;
  }

  private ProfileCredentialsProvider rebuildProvider() {
    return this.credentialsProvider = ProfileCredentialsProvider.builder()
      .profileName(profile)
      .build();
  }

  private void doResolveCredentials() {
    credentials = credentialsProvider.resolveCredentials();
  }
  
  private void tryLoginAndResolve(SdkException e) {
    if (trySsoLogin()) {
      rebuildProvider();
      doResolveCredentials();
    } else {
      throw e;
    }
  }

  private boolean trySsoLogin() {
    if (SSO_LOGIN != null) {
      try {
        List<String> command = new LinkedList<>(asList(SSO_LOGIN));
        command.add(profile);
        new ProcessBuilder()
          .command(command)
          .inheritIO()
          .start()
          .waitFor(30, SECONDS);
        return true;
      } catch (Exception unableToLogin) {
        // cannot login
      }
    }
    return false;
  }

  private static String[] buildBaseAwsLoginCommand() {
    try {
      String pathSeparator = System.getProperty("path.separator");
      return Stream.of(System.getenv("PATH").split(pathSeparator, -1))
        .map(Path::of)
        .flatMap(p -> {
          try {
            return Files.list(p);
          } catch (IOException e) {
            return Stream.empty();
          }
        })
        .filter(file -> file.getFileName().toString().toLowerCase().startsWith("aws"))
        .findFirst()
        .map(aws -> new String[] { aws.toString(), "sso", "login", "--profile" })
        .orElse(null);
    } catch (Throwable th) {
      return null;
    }
  }
}
