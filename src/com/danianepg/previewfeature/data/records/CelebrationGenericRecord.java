package com.danianepg.previewfeature.data.records;

import java.time.Month;

/**
 * Generic record
 * @author Daniane P. Gomes
 *
 * @param <T>
 */
public record CelebrationGenericRecord<T>(T contents, String name, Integer day, Month month) {
}
