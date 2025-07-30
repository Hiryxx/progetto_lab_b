package server.connection.response;

/**
 * Sendable is an interface that represents a response that can be sent over a network connection.
 * It is implemented by the classes that represent different types of responses.
 */
public sealed interface Sendable permits SingleResponse, MultiResponse {
}
