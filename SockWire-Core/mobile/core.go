// Package mobile is the gomobile entrypoint for SockWire Core.
package mobile

import (
	"context"
	"sync"

	"github.com/shahradelahi/wiresocks"
)

var (
	mu     sync.Mutex
	cancel context.CancelFunc
)

// Start parses a wg-quick .conf text, starts the tunnel and SOCKS5 proxy.
func Start(confText string) string {
	mu.Lock()
	defer mu.Unlock()

	if cancel != nil {
		return "already running"
	}

	// Parse the wg-quick style config text
	cfg, err := wiresocks.ParseConfigFromText(confText)
	if err != nil {
		return "config: " + err.Error()
	}

	// Create a cancellable context to control the lifecycle
	ctx, cancelFunc := context.WithCancel(context.Background())

	// Create the WireSocks instance
	ws, err := wiresocks.NewWireSocks(
		wiresocks.WithContext(ctx),
		wiresocks.WithWireguardConfig(cfg),
	)
	if err != nil {
		cancelFunc()
		return "init: " + err.Error()
	}

	// Run the proxy server in a goroutine
	go func() {
		if err := ws.Run(); err != nil {
			// In a real scenario, we might want to log this.
			// For now, we just stop.
		}
	}()

	cancel = cancelFunc
	return ""
}

// Stop halts the tunnel and proxy.
func Stop() string {
	mu.Lock()
	defer mu.Unlock()

	if cancel == nil {
		return ""
	}

	cancel()
	cancel = nil
	return ""
}

// IsRunning reports engine state.
func IsRunning() bool {
	mu.Lock()
	defer mu.Unlock()
	return cancel != nil
}

// Version is the SockWire Core version string.
func Version() string { return "sockwire-core-wiresocks-1.0.0" }
