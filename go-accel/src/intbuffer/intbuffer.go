// Int buffer with a fixed capacity.  When the buffer is full,
// new values will overwrite the oldest values in the buffer.
// Operations are not thread safe.
package intbuffer

type Buffer struct {
	data []int
	write_index int
	full bool
}

// Creates a new Buffer with the given capacity.  Once the buffer
// reaches the capacity, Add will overwrite the oldest value.
func New(capacity int) *Buffer {
	return &Buffer{make([]int, capacity), 0, false}
}

// Adds the given value to this buffer.  If the buffer is full,
// the oldest value in this Buffer will be overwritten
func (buffer *Buffer) Add(value int) {
	length := len(buffer.data)

	buffer.data[buffer.write_index] = value
	
	if !buffer.full && buffer.write_index == length - 1 {
		buffer.full = true
	}

	buffer.write_index = (buffer.write_index + 1) % length
}

// Folds over the elements in this buffer from oldest to newest.
// The provided function takes the value so far (initially 0) and the next
// value in the buffer, and returns a new value.  For example, sum can be implemented like:
// buffer.Fold(func(acc, value int) int {
//	return acc + value
// })
func (buffer *Buffer) Fold(f func(acc, value int) int) int {
	var start, length int
	if buffer.full {
		start = buffer.write_index
		length = len(buffer.data)
	} else {
		start = 0
		length = buffer.write_index
	}

	acc := 0
	for i := 0; i < length; i ++ {
		acc = f(acc, buffer.data[(i + start) % len(buffer.data)])
	}

	return acc
}
