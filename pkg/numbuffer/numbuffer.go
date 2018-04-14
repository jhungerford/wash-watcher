// Int buffer with a fixed capacity.  When the buffer is full,
// new values will overwrite the oldest values in the buffer.
// Operations are not thread safe.
package numbuffer

type Buffer struct {
	data       []float64
	writeIndex int
	full       bool
}

// Creates a new Buffer with the given capacity.  Once the buffer
// reaches the capacity, Add will overwrite the oldest value.
func New(capacity int) *Buffer {
	return &Buffer{make([]float64, capacity), 0, false}
}

// Adds the given value to this buffer.  If the buffer is full,
// the oldest value in this Buffer will be overwritten
func (buffer *Buffer) Add(value float64) {
	length := len(buffer.data)

	buffer.data[buffer.writeIndex] = value
	
	if !buffer.full && buffer.writeIndex == length - 1 {
		buffer.full = true
	}

	buffer.writeIndex = (buffer.writeIndex + 1) % length
}

func (buffer *Buffer) Length() int {
	if buffer.full {
		return len(buffer.data)
	}

	return buffer.writeIndex
}

// Folds over the elements in this buffer from oldest to newest.
// The provided function takes the value so far (initially 0) and the next
// value in the buffer, and returns a new value.  For example, sum can be implemented like:
// buffer.Fold(func(acc, value int) int {
//	return acc + value
// })
func (buffer *Buffer) Fold(f func(acc, value float64) float64) float64 {
	var start, length int
	if buffer.full {
		start = buffer.writeIndex
		length = len(buffer.data)
	} else {
		start = 0
		length = buffer.writeIndex
	}

	var acc float64 = 0
	for i := 0; i < length; i ++ {
		acc = f(acc, buffer.data[(i + start) % len(buffer.data)])
	}

	return acc
}
