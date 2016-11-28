/**
 * This package contains {@link rudi.process.LineProcessor}s that deals
 * with comments.
 *
 * When composed in the {@link rudi.process.DelegatingLineProcessor}, it should appear
 * in the following (relative) order:
 * 1. {@link rudi.process.comment.StartCommentModeLineProcessor}
 * 2. {@link rudi.process.comment.EndCommentModeLineProcessor}
 * 3. {@link rudi.process.comment.IgnoreCommentLineProcessor}
 */
package rudi.process.comment;