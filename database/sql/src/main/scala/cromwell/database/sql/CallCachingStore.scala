package cromwell.database.sql

import cromwell.database.sql.tables.{CallCachingHashEntry, CallCachingResultMetaInfoEntry, CallCachingResultSimpletonEntry}

import scala.concurrent.{ExecutionContext, Future}

trait CallCachingStore {
  def addToCache(callCachingResultMetaInfo: CallCachingResultMetaInfoEntry,
                 hashesToInsert: Int => Iterable[CallCachingHashEntry],
                 resultToInsert: Int => Iterable[CallCachingResultSimpletonEntry])
                (implicit ec: ExecutionContext): Future[Unit]

  def metaInfoIdsMatchingHashes(hashKeyValuePairs: Seq[(String, String)])
                               (implicit ec: ExecutionContext): Future[Seq[Seq[Int]]]

  def fetchCachedResult(callCachingResultMetaInfoId: Int)(implicit ec: ExecutionContext):
  Future[(Option[CallCachingResultMetaInfoEntry], Seq[CallCachingResultSimpletonEntry])]
}
