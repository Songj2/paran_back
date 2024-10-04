package com.paranmanzang.fileservice.model.repository.custom;

import com.paranmanzang.fileservice.model.entity.File;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileCustomRepository {
    Flux<File> findByRefId(Long refId, int type);
    Mono<?> findByPath(String path);
    Mono<?> deleteByPath(String path);
}
