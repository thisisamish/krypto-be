package com.groupeight.krypto.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class SoftDeletable extends BaseEntity {
  @Column(name = "deleted_at")
  private Instant deletedAt;

  public boolean isDeleted() { return deletedAt != null; }
  public void softDelete() { this.deletedAt = Instant.now(); }
}

